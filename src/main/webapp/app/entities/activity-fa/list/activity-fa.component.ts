import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivityFa } from '../activity-fa.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ActivityFaService } from '../service/activity-fa.service';
import { ActivityFaDeleteDialogComponent } from '../delete/activity-fa-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'fa-activity-fa',
  templateUrl: './activity-fa.component.html',
})
export class ActivityFaComponent implements OnInit {
  activities: IActivityFa[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected activityService: ActivityFaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.activities = [];
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

    this.activityService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IActivityFa[]>) => {
          this.isLoading = false;
          this.paginateActivities(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.activities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IActivityFa): number {
    return item.id!;
  }

  delete(activity: IActivityFa): void {
    const modalRef = this.modalService.open(ActivityFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.activity = activity;
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

  protected paginateActivities(data: IActivityFa[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.activities.push(d);
      }
    }
  }
}
