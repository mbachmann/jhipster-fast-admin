import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactPersonFa } from '../contact-person-fa.model';
import { ContactPersonFaService } from '../service/contact-person-fa.service';
import { ContactPersonFaDeleteDialogComponent } from '../delete/contact-person-fa-delete-dialog.component';

@Component({
  selector: 'fa-contact-person-fa',
  templateUrl: './contact-person-fa.component.html',
})
export class ContactPersonFaComponent implements OnInit {
  contactPeople?: IContactPersonFa[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactPersonService: ContactPersonFaService,
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
          (res: HttpResponse<IContactPersonFa[]>) => {
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
      (res: HttpResponse<IContactPersonFa[]>) => {
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

  trackId(index: number, item: IContactPersonFa): number {
    return item.id!;
  }

  delete(contactPerson: IContactPersonFa): void {
    const modalRef = this.modalService.open(ContactPersonFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactPerson = contactPerson;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
