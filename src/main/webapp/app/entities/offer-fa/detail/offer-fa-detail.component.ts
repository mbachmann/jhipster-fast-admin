import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOfferFa } from '../offer-fa.model';

@Component({
  selector: 'fa-offer-fa-detail',
  templateUrl: './offer-fa-detail.component.html',
})
export class OfferFaDetailComponent implements OnInit {
  offer: IOfferFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offer }) => {
      this.offer = offer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
