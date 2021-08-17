import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomField } from '../custom-field.model';

@Component({
  selector: 'fa-custom-field-detail',
  templateUrl: './custom-field-detail.component.html',
})
export class CustomFieldDetailComponent implements OnInit {
  customField: ICustomField | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customField }) => {
      this.customField = customField;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
