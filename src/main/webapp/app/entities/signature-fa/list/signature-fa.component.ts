import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISignatureFa } from '../signature-fa.model';
import { SignatureFaService } from '../service/signature-fa.service';
import { SignatureFaDeleteDialogComponent } from '../delete/signature-fa-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'fa-signature-fa',
  templateUrl: './signature-fa.component.html',
})
export class SignatureFaComponent implements OnInit {
  signatures?: ISignatureFa[];
  isLoading = false;

  constructor(protected signatureService: SignatureFaService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.signatureService.query().subscribe(
      (res: HttpResponse<ISignatureFa[]>) => {
        this.isLoading = false;
        this.signatures = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISignatureFa): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(signature: ISignatureFa): void {
    const modalRef = this.modalService.open(SignatureFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.signature = signature;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
