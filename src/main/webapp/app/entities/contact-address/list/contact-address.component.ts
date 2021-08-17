import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAddress } from '../contact-address.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ContactAddressService } from '../service/contact-address.service';
import { ContactAddressDeleteDialogComponent } from '../delete/contact-address-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'fa-contact-address',
  templateUrl: './contact-address.component.html',
})
export class ContactAddressComponent implements OnInit {
  contactAddresses: IContactAddress[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected contactAddressService: ContactAddressService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.contactAddresses = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.contactAddressService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IContactAddress[]>) => {
          this.isLoading = false;
          this.paginateContactAddresses(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.contactAddresses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IContactAddress): number {
    return item.id!;
  }

  delete(contactAddress: IContactAddress): void {
    const modalRef = this.modalService.open(ContactAddressDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactAddress = contactAddress;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateContactAddresses(data: IContactAddress[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.contactAddresses.push(d);
      }
    }
  }
}
