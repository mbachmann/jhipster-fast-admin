import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';

@Component({
  selector: 'fa-descriptive-document-text-fa-detail',
  templateUrl: './descriptive-document-text-fa-detail.component.html',
})
export class DescriptiveDocumentTextFaDetailComponent implements OnInit {
  descriptiveDocumentText: IDescriptiveDocumentTextFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ descriptiveDocumentText }) => {
      this.descriptiveDocumentText = descriptiveDocumentText;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
