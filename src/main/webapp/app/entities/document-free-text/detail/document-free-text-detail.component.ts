import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentFreeText } from '../document-free-text.model';

@Component({
  selector: 'fa-document-free-text-detail',
  templateUrl: './document-free-text-detail.component.html',
})
export class DocumentFreeTextDetailComponent implements OnInit {
  documentFreeText: IDocumentFreeText | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentFreeText }) => {
      this.documentFreeText = documentFreeText;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
