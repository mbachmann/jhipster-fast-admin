import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentTextFa } from '../document-text-fa.model';

@Component({
  selector: 'fa-document-text-fa-detail',
  templateUrl: './document-text-fa-detail.component.html',
})
export class DocumentTextFaDetailComponent implements OnInit {
  documentText: IDocumentTextFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentText }) => {
      this.documentText = documentText;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
