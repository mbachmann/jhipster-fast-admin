import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISignatureFa } from '../signature-fa.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'fa-signature-fa-detail',
  templateUrl: './signature-fa-detail.component.html',
})
export class SignatureFaDetailComponent implements OnInit {
  signature: ISignatureFa | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signature }) => {
      this.signature = signature;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
