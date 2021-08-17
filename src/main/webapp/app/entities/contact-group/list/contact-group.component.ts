import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactGroup } from '../contact-group.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ContactGroupService } from '../service/contact-group.service';
import { ContactGroupDeleteDialogComponent } from '../delete/contact-group-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'fa-contact-group',
  templateUrl: './contact-group.component.html',
})
export class ContactGroupComponent implements OnInit {
  contactGroups: IContactGroup[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected contactGroupService: ContactGroupService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.contactGroups = [];
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

    this.contactGroupService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IContactGroup[]>) => {
          this.isLoading = false;
          this.paginateContactGroups(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.contactGroups = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IContactGroup): number {
    return item.id!;
  }

  delete(contactGroup: IContactGroup): void {
    const modalRef = this.modalService.open(ContactGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactGroup = contactGroup;
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

  protected paginateContactGroups(data: IContactGroup[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.contactGroups.push(d);
      }
    }
  }
}
