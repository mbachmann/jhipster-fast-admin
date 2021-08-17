import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivityFa } from '../activity-fa.model';

@Component({
  selector: 'fa-activity-fa-detail',
  templateUrl: './activity-fa-detail.component.html',
})
export class ActivityFaDetailComponent implements OnInit {
  activity: IActivityFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.activity = activity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
