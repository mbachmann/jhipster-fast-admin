import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogUnitFa } from '../catalog-unit-fa.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CatalogUnitFaService } from '../service/catalog-unit-fa.service';
import { CatalogUnitFaDeleteDialogComponent } from '../delete/catalog-unit-fa-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'fa-catalog-unit-fa',
  templateUrl: './catalog-unit-fa.component.html',
})
export class CatalogUnitFaComponent implements OnInit {
  catalogUnits: ICatalogUnitFa[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected catalogUnitService: CatalogUnitFaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.catalogUnits = [];
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

    this.catalogUnitService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ICatalogUnitFa[]>) => {
          this.isLoading = false;
          this.paginateCatalogUnits(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.catalogUnits = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICatalogUnitFa): number {
    return item.id!;
  }

  delete(catalogUnit: ICatalogUnitFa): void {
    const modalRef = this.modalService.open(CatalogUnitFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.catalogUnit = catalogUnit;
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

  protected paginateCatalogUnits(data: ICatalogUnitFa[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.catalogUnits.push(d);
      }
    }
  }
}
