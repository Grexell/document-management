import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Document} from '../model/document';
import {UserService} from '../service/user.service';
import {FileService} from '../service/file.service';
import {DocumentService} from '../service/document.service';

@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent implements OnInit {

  @Input() document: Document;
  @Output() delete = new EventEmitter<Document>();
  @ViewChild('download') link: ElementRef;
  clicked = false;

  username: string;
  acceptDate: Date;
  validToDate: Date;
  signs: string;
  owner: string;
  notSign: string;
  isOwner: boolean;
  canSign: boolean;
  canArchive: boolean;

  constructor(private userService: UserService, private fileService: FileService, private documentService: DocumentService) { }

  ngOnInit() {
    this.username = this.userService.getDecodedAccessToken()['user_name'];
    this.acceptDate = this.document.acceptDate;
    this.validToDate = this.document.validTo;
    this.signs = '' + this.document.signList.length + '/' + this.document.needSignList.length;
    this.notSign = this.document.needSignList.length !== this.document.signList.length  && this.document.needSignList.filter(value => !this.document.signList.map(user => user.username).includes(value.username)).map(user => user.name).join(', ');
    this.isOwner = this.document.creator === this.username;
    this.canArchive = this.isOwner && !this.document.archived;
    this.canSign = this.document.needSignList.filter(user => user.username === this.username).length
      && !this.document.signList.filter(user => user.username === this.username).length;
    this.owner = this.document.owner;
  }

  toggleClicked() {
    this.clicked = !this.clicked;
  }

  downloadFile() {
    this.fileService.load(this.document.name, this.link);
  }

  uploadFile(file: File) {
    this.fileService.upload(this.document.name, file).subscribe(value => {});
  }

  archiveDocument() {
    this.documentService.archive(this.document).subscribe(value => {
      this.delete.emit(this.document);
    });
  }

  signDocument() {
    this.documentService.sign(this.document).subscribe(value => {
      this.document.signList.push(this.username);
      this.signs = '' + this.document.signList.length + '/' + this.document.needSignList.length;
      this.notSign = this.document.needSignList.filter(value => !this.document.signList.includes(value)).map(user => user.name).join(', ');
      this.canSign = false;
    });
  }

  deleteDocument() {
    this.documentService.delete(this.document).subscribe(value => {
      this.delete.emit(this.document);
    });
  }
}
