import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVatFa } from '../vat-fa.model';

@Component({
  selector: 'fa-vat-fa-detail',
  templateUrl: './vat-fa-detail.component.html',
})
export class VatFaDetailComponent implements OnInit {
  vat: IVatFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vat }) => {
      this.vat = vat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
