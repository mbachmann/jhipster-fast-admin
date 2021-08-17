import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomFieldValue } from '../custom-field-value.model';

@Component({
  selector: 'fa-custom-field-value-detail',
  templateUrl: './custom-field-value-detail.component.html',
})
export class CustomFieldValueDetailComponent implements OnInit {
  customFieldValue: ICustomFieldValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customFieldValue }) => {
      this.customFieldValue = customFieldValue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
