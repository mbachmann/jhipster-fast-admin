import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILayoutFa } from '../layout-fa.model';

@Component({
  selector: 'fa-layout-fa-detail',
  templateUrl: './layout-fa-detail.component.html',
})
export class LayoutFaDetailComponent implements OnInit {
  layout: ILayoutFa | null = null;

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
