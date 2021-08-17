import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFreeTextFa } from '../free-text-fa.model';

@Component({
  selector: 'fa-free-text-fa-detail',
  templateUrl: './free-text-fa-detail.component.html',
})
export class FreeTextFaDetailComponent implements OnInit {
  freeText: IFreeTextFa | null = null;

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
