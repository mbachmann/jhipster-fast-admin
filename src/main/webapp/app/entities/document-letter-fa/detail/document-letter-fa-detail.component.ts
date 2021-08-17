import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentLetterFa } from '../document-letter-fa.model';

@Component({
  selector: 'fa-document-letter-fa-detail',
  templateUrl: './document-letter-fa-detail.component.html',
})
export class DocumentLetterFaDetailComponent implements OnInit {
  documentLetter: IDocumentLetterFa | null = null;

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
