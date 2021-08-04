import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountryMySuffix } from '../country-my-suffix.model';
import { CountryMySuffixService } from '../service/country-my-suffix.service';
import { CountryMySuffixDeleteDialogComponent } from '../delete/country-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-country-my-suffix',
  templateUrl: './country-my-suffix.component.html',
})
export class CountryMySuffixComponent implements OnInit {
  countries?: ICountryMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected countryService: CountryMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.countryService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<ICountryMySuffix[]>) => {
            this.isLoading = false;
            this.countries = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.countryService.query().subscribe(
      (res: HttpResponse<ICountryMySuffix[]>) => {
        this.isLoading = false;
        this.countries = res.body ?? [];
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

  trackId(index: number, item: ICountryMySuffix): number {
    return item.id!;
  }

  delete(country: ICountryMySuffix): void {
    const modalRef = this.modalService.open(CountryMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.country = country;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
