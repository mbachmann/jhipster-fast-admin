import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegionMySuffix } from '../region-my-suffix.model';
import { RegionMySuffixService } from '../service/region-my-suffix.service';
import { RegionMySuffixDeleteDialogComponent } from '../delete/region-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-region-my-suffix',
  templateUrl: './region-my-suffix.component.html',
})
export class RegionMySuffixComponent implements OnInit {
  regions?: IRegionMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(protected regionService: RegionMySuffixService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.regionService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IRegionMySuffix[]>) => {
            this.isLoading = false;
            this.regions = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.regionService.query().subscribe(
      (res: HttpResponse<IRegionMySuffix[]>) => {
        this.isLoading = false;
        this.regions = res.body ?? [];
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

  trackId(index: number, item: IRegionMySuffix): number {
    return item.id!;
  }

  delete(region: IRegionMySuffix): void {
    const modalRef = this.modalService.open(RegionMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.region = region;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
