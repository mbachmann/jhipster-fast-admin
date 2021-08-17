import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IValueAddedTax } from '../value-added-tax.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ValueAddedTaxService } from '../service/value-added-tax.service';
import { ValueAddedTaxDeleteDialogComponent } from '../delete/value-added-tax-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'fa-value-added-tax',
  templateUrl: './value-added-tax.component.html',
})
export class ValueAddedTaxComponent implements OnInit {
  valueAddedTaxes: IValueAddedTax[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected valueAddedTaxService: ValueAddedTaxService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.valueAddedTaxes = [];
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

    this.valueAddedTaxService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IValueAddedTax[]>) => {
          this.isLoading = false;
          this.paginateValueAddedTaxes(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.valueAddedTaxes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IValueAddedTax): number {
    return item.id!;
  }

  delete(valueAddedTax: IValueAddedTax): void {
    const modalRef = this.modalService.open(ValueAddedTaxDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.valueAddedTax = valueAddedTax;
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

  protected paginateValueAddedTaxes(data: IValueAddedTax[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.valueAddedTaxes.push(d);
      }
    }
  }
}
