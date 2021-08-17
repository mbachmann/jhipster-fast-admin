import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentPosition } from '../document-position.model';

@Component({
  selector: 'fa-document-position-detail',
  templateUrl: './document-position-detail.component.html',
})
export class DocumentPositionDetailComponent implements OnInit {
  documentPosition: IDocumentPosition | null = null;

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
