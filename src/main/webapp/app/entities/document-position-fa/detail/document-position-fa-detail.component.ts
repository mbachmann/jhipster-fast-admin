import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentPositionFa } from '../document-position-fa.model';

@Component({
  selector: 'fa-document-position-fa-detail',
  templateUrl: './document-position-fa-detail.component.html',
})
export class DocumentPositionFaDetailComponent implements OnInit {
  documentPosition: IDocumentPositionFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentPosition }) => {
      this.documentPosition = documentPosition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
