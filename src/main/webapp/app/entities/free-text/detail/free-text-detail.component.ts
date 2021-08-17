import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFreeText } from '../free-text.model';

@Component({
  selector: 'fa-free-text-detail',
  templateUrl: './free-text-detail.component.html',
})
export class FreeTextDetailComponent implements OnInit {
  freeText: IFreeText | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ freeText }) => {
      this.freeText = freeText;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
