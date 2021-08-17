import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentFreeTextFa } from '../document-free-text-fa.model';

@Component({
  selector: 'fa-document-free-text-fa-detail',
  templateUrl: './document-free-text-fa-detail.component.html',
})
export class DocumentFreeTextFaDetailComponent implements OnInit {
  documentFreeText: IDocumentFreeTextFa | null = null;

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
