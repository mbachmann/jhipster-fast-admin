import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactPersonMySuffix } from '../contact-person-my-suffix.model';
import { ContactPersonMySuffixService } from '../service/contact-person-my-suffix.service';
import { ContactPersonMySuffixDeleteDialogComponent } from '../delete/contact-person-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-contact-person-my-suffix',
  templateUrl: './contact-person-my-suffix.component.html',
})
export class ContactPersonMySuffixComponent implements OnInit {
  contactPeople?: IContactPersonMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactPersonService: ContactPersonMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.contactPersonService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IContactPersonMySuffix[]>) => {
            this.isLoading = false;
            this.contactPeople = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.contactPersonService.query().subscribe(
      (res: HttpResponse<IContactPersonMySuffix[]>) => {
        this.isLoading = false;
        this.contactPeople = res.body ?? [];
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

  trackId(index: number, item: IContactPersonMySuffix): number {
    return item.id!;
  }

  delete(contactPerson: IContactPersonMySuffix): void {
    const modalRef = this.modalService.open(ContactPersonMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactPerson = contactPerson;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
