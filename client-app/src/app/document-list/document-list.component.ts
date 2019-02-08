import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DocumentService} from '../service/document.service';
import {UserService} from '../service/user.service';
import {Document} from '../model/document';
import {FormControl} from "@angular/forms";
import {debounceTime} from "rxjs/operators";

@Component({
  selector: 'app-document-list',
  templateUrl: './document-list.component.html',
  styleUrls: ['./document-list.component.css']
})
export class DocumentListComponent implements OnInit {

  private documents: Document[] = [];
  private filteredDocuments: Document[] = [];
  private filter: FormControl;
  constructor(private route: ActivatedRoute, private documentService: DocumentService, private userService: UserService) {
  }

  ngOnInit() {
    this.route.params.subscribe(value => {
      const command = value['command'];

      this.filter = new FormControl('');
      this.filter.valueChanges
        .pipe(debounceTime(100))
        .subscribe(filterValue => {
          this.filterDocuments(filterValue);
        });

      this.loadDocuments(command).subscribe(data => {
          this.documents = data;
        this.filteredDocuments = data;
      }, error => {
        this.userService.refreshToken().subscribe(value1 => {
          this.loadDocuments(command).subscribe(docs => {
            this.documents = docs;
            this.filteredDocuments = docs;
          });
        });
      });
    });
  }

  loadDocuments(command: string) {
    let subscription;
    switch (command) {
      case 'me':
        subscription = this.documentService.getMy();
        break;
      case 'active':
        subscription = this.documentService.getActive();
        break;
      case 'sign':
        subscription = this.documentService.getSign();
        break;
      case 'archive':
        subscription = this.documentService.getArchived();
        break;
    }
    return subscription;
  }

  filterDocuments(filterValue: string) {
    if (filterValue) {
      this.filteredDocuments = this.documents.filter(document =>
        document.title.includes(filterValue) ||
        document.description.includes(filterValue) ||
        document.creator.includes(filterValue)
      );
    } else {
      this.filteredDocuments = this.documents;
    }
  }

  deleteDocument(document: Document) {
    this.documents.splice(this.documents.indexOf(document), 1);
    this.filterDocuments(this.filter.value);
  }
}
