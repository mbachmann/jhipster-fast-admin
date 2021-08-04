import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactMySuffix } from '../contact-my-suffix.model';
import { ContactMySuffixService } from '../service/contact-my-suffix.service';
import { ContactMySuffixDeleteDialogComponent } from '../delete/contact-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-contact-my-suffix',
  templateUrl: './contact-my-suffix.component.html',
})
export class ContactMySuffixComponent implements OnInit {
  contacts?: IContactMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactService: ContactMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.contactService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IContactMySuffix[]>) => {
            this.isLoading = false;
            this.contacts = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.contactService.query().subscribe(
      (res: HttpResponse<IContactMySuffix[]>) => {
        this.isLoading = false;
        this.contacts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IContactMySuffix): number {
    return item.id!;
  }

  delete(contact: IContactMySuffix): void {
    const modalRef = this.modalService.open(ContactMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contact = contact;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
