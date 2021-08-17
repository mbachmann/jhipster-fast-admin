import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReportingActivity } from '../reporting-activity.model';

@Component({
  selector: 'fa-reporting-activity-detail',
  templateUrl: './reporting-activity-detail.component.html',
})
export class ReportingActivityDetailComponent implements OnInit {
  reportingActivity: IReportingActivity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingActivity }) => {
      this.reportingActivity = reportingActivity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
