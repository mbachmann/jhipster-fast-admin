import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEffortFa } from '../effort-fa.model';

@Component({
  selector: 'fa-effort-fa-detail',
  templateUrl: './effort-fa-detail.component.html',
})
export class EffortFaDetailComponent implements OnInit {
  effort: IEffortFa | null = null;

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
