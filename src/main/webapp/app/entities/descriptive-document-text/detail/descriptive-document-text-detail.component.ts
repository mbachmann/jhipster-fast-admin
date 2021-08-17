import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDescriptiveDocumentText } from '../descriptive-document-text.model';

@Component({
  selector: 'fa-descriptive-document-text-detail',
  templateUrl: './descriptive-document-text-detail.component.html',
})
export class DescriptiveDocumentTextDetailComponent implements OnInit {
  descriptiveDocumentText: IDescriptiveDocumentText | null = null;

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
