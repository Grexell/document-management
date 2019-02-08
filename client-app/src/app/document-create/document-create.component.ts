import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Document} from '../model/document';
import {Access} from '../model/access';
import {AccessService} from '../service/access.service';
import {FormControl, FormGroup} from '@angular/forms';
import {debounceTime} from 'rxjs/operators';
import {FileService} from '../service/file.service';
import {DocumentService} from '../service/document.service';

@Component({
  selector: 'app-document-create',
  templateUrl: './document-create.component.html',
  styleUrls: ['./document-create.component.css']
})
export class DocumentCreateComponent implements OnInit {

  createForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
    accessFilter: new FormControl(''),
    accesses: new FormControl([]),
    userFilter: new FormControl(''),
    users: new FormControl([]),
    acceptDate: new FormControl(''),
    validTo: new FormControl(''),
    file: new FormControl(null)
  });

  users: string[] = [];
  filteredUsers: any[] = [];
  accesses: Access[] = [];
  filteredAccesses: Access[] = [];

  file = null;
  loading = false;

  constructor(private accessService: AccessService, private cd: ChangeDetectorRef, private fileService: FileService,
              private documentService: DocumentService) {
    this.createForm.controls.accessFilter.valueChanges
      .pipe(debounceTime(100))
      .subscribe(value => {
        this.filteredAccesses = this.accesses.filter(access => access.post.includes(value) || access.department.includes(value));
      });

    this.createForm.controls.userFilter.valueChanges
      .pipe(debounceTime(100))
      .subscribe(value => {
        this.filteredUsers = this.users.filter(user => user.includes(value));
      });
  }

  ngOnInit() {
    this.accessService.getAccesses().subscribe(value => {
      this.accesses = value;
      this.filteredAccesses = this.accesses;
    });

    this.documentService.userList().subscribe(value => {
      this.users = value;
      this.filteredUsers = this.users;
    });
  }

  setFile(file: any) {
    this.file = file;
  }

  public parseDate(dateString: string): Date | null {
    const data = dateString.split(new RegExp('[\- .:]+'));

    const result = data.length > 0;
    if (result) {
      try {
        return new Date(new Date().getFullYear(),
          +data[1] ? +data[1] - 1 : 0,
          +data[2] ? +data[2] : 1,
          +data[3] ? +data[3] : 0,
          +data[4] ? +data[4] : 0,
          +data[5] ? +data[5] : 0,
          +data[6] ? +data[6] : 0);
      } catch (e) {
        return null;
      }
    }
    return null;
  }



  submit() {
    const document = new Document();
    const that = this;
    document.title = this.createForm.value.title;
    document.description = this.createForm.value.description;
    document.accesses = this.createForm.value.accesses;
    document.needSignList = this.createForm.value.users;
    document.signList = null;
    document.acceptDate = this.parseDate(this.createForm.value.acceptDate);
    document.validTo = this.parseDate(this.createForm.value.validTo);
    document.name = document.title + '/' + this.file.name;
    this.loading = true;
    this.documentService.create(document).subscribe(value => {
      that.fileService.upload(document.name, that.file).subscribe(value1 => {
        that.loading = false;
      });
    });
  }
}
