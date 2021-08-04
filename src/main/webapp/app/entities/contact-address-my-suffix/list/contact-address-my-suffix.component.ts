import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAddressMySuffix } from '../contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from '../service/contact-address-my-suffix.service';
import { ContactAddressMySuffixDeleteDialogComponent } from '../delete/contact-address-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-contact-address-my-suffix',
  templateUrl: './contact-address-my-suffix.component.html',
})
export class ContactAddressMySuffixComponent implements OnInit {
  contactAddresses?: IContactAddressMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactAddressService: ContactAddressMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.contactAddressService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IContactAddressMySuffix[]>) => {
            this.isLoading = false;
            this.contactAddresses = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.contactAddressService.query().subscribe(
      (res: HttpResponse<IContactAddressMySuffix[]>) => {
        this.isLoading = false;
        this.contactAddresses = res.body ?? [];
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

  trackId(index: number, item: IContactAddressMySuffix): number {
    return item.id!;
  }

  delete(contactAddress: IContactAddressMySuffix): void {
    const modalRef = this.modalService.open(ContactAddressMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactAddress = contactAddress;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
