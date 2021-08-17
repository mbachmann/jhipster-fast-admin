import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkingHour } from '../working-hour.model';

@Component({
  selector: 'fa-working-hour-detail',
  templateUrl: './working-hour-detail.component.html',
})
export class WorkingHourDetailComponent implements OnInit {
  workingHour: IWorkingHour | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingHour }) => {
      this.workingHour = workingHour;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
