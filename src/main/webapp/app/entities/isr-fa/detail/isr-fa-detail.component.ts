import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsrFa } from '../isr-fa.model';

@Component({
  selector: 'fa-isr-fa-detail',
  templateUrl: './isr-fa-detail.component.html',
})
export class IsrFaDetailComponent implements OnInit {
  isr: IIsrFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isr }) => {
      this.isr = isr;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
