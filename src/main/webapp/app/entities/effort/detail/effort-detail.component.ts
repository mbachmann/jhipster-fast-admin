import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEffort } from '../effort.model';

@Component({
  selector: 'fa-effort-detail',
  templateUrl: './effort-detail.component.html',
})
export class EffortDetailComponent implements OnInit {
  effort: IEffort | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ effort }) => {
      this.effort = effort;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
