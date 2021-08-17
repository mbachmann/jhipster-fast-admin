import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILayout } from '../layout.model';

@Component({
  selector: 'fa-layout-detail',
  templateUrl: './layout-detail.component.html',
})
export class LayoutDetailComponent implements OnInit {
  layout: ILayout | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layout }) => {
      this.layout = layout;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
