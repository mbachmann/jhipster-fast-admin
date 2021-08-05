import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomFieldMySuffix } from '../custom-field-my-suffix.model';

@Component({
  selector: 'jhl-custom-field-my-suffix-detail',
  templateUrl: './custom-field-my-suffix-detail.component.html',
})
export class CustomFieldMySuffixDetailComponent implements OnInit {
  customField: ICustomFieldMySuffix | null = null;

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
