import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISignatureFa, SignatureFa } from '../signature-fa.model';
import { SignatureFaService } from '../service/signature-fa.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUserFa } from 'app/entities/application-user-fa/application-user-fa.model';
import { ApplicationUserFaService } from 'app/entities/application-user-fa/service/application-user-fa.service';

@Component({
  selector: 'fa-signature-fa-update',
  templateUrl: './signature-fa-update.component.html',
})
export class SignatureFaUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUserFa[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    signatureImage: [],
    signatureImageContentType: [],
    width: [],
    heigth: [],
    userName: [],
    applicationUser: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected signatureService: SignatureFaService,
    protected applicationUserService: ApplicationUserFaService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signature }) => {
      this.updateForm(signature);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('fastAdminApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const signature = this.createFromForm();
    if (signature.id !== undefined) {
      this.subscribeToSaveResponse(this.signatureService.update(signature));
    } else {
      this.subscribeToSaveResponse(this.signatureService.create(signature));
    }
  }

  trackApplicationUserFaById(index: number, item: IApplicationUserFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignatureFa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(signature: ISignatureFa): void {
    this.editForm.patchValue({
      id: signature.id,
      remoteId: signature.remoteId,
      signatureImage: signature.signatureImage,
      signatureImageContentType: signature.signatureImageContentType,
      width: signature.width,
      heigth: signature.heigth,
      userName: signature.userName,
      applicationUser: signature.applicationUser,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserFaToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      signature.applicationUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUserFa[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUserFa[]) =>
          this.applicationUserService.addApplicationUserFaToCollectionIfMissing(
            applicationUsers,
            this.editForm.get('applicationUser')!.value
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUserFa[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): ISignatureFa {
    return {
      ...new SignatureFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      signatureImageContentType: this.editForm.get(['signatureImageContentType'])!.value,
      signatureImage: this.editForm.get(['signatureImage'])!.value,
      width: this.editForm.get(['width'])!.value,
      heigth: this.editForm.get(['heigth'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
    };
  }
}
