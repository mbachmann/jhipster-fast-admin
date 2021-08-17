import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentLetter } from '../document-letter.model';

@Component({
  selector: 'fa-document-letter-detail',
  templateUrl: './document-letter-detail.component.html',
})
export class DocumentLetterDetailComponent implements OnInit {
  documentLetter: IDocumentLetter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentLetter }) => {
      this.documentLetter = documentLetter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
