import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomFieldFa } from '../custom-field-fa.model';

@Component({
  selector: 'fa-custom-field-fa-detail',
  templateUrl: './custom-field-fa-detail.component.html',
})
export class CustomFieldFaDetailComponent implements OnInit {
  customField: ICustomFieldFa | null = null;

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
