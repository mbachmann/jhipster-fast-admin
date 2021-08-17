import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportingActivity } from '../reporting-activity.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ReportingActivityService } from '../service/reporting-activity.service';
import { ReportingActivityDeleteDialogComponent } from '../delete/reporting-activity-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'fa-reporting-activity',
  templateUrl: './reporting-activity.component.html',
})
export class ReportingActivityComponent implements OnInit {
  reportingActivities: IReportingActivity[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected reportingActivityService: ReportingActivityService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.reportingActivities = [];
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

    this.reportingActivityService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IReportingActivity[]>) => {
          this.isLoading = false;
          this.paginateReportingActivities(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.reportingActivities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IReportingActivity): number {
    return item.id!;
  }

  delete(reportingActivity: IReportingActivity): void {
    const modalRef = this.modalService.open(ReportingActivityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reportingActivity = reportingActivity;
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

  protected paginateReportingActivities(data: IReportingActivity[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.reportingActivities.push(d);
      }
    }
  }
}
