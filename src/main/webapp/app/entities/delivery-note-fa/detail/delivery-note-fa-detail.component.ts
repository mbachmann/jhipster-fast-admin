import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryNoteFa } from '../delivery-note-fa.model';

@Component({
  selector: 'fa-delivery-note-fa-detail',
  templateUrl: './delivery-note-fa-detail.component.html',
})
export class DeliveryNoteFaDetailComponent implements OnInit {
  deliveryNote: IDeliveryNoteFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryNote }) => {
      this.deliveryNote = deliveryNote;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
