import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsr } from '../isr.model';

@Component({
  selector: 'fa-isr-detail',
  templateUrl: './isr-detail.component.html',
})
export class IsrDetailComponent implements OnInit {
  isr: IIsr | null = null;

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
