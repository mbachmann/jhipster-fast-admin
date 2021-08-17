import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentText } from '../document-text.model';

@Component({
  selector: 'fa-document-text-detail',
  templateUrl: './document-text-detail.component.html',
})
export class DocumentTextDetailComponent implements OnInit {
  documentText: IDocumentText | null = null;

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
