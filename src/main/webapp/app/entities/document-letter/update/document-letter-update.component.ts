import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDocumentLetter, DocumentLetter } from '../document-letter.model';
import { DocumentLetterService } from '../service/document-letter.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';
import { IContactAddress } from 'app/entities/contact-address/contact-address.model';
import { ContactAddressService } from 'app/entities/contact-address/service/contact-address.service';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ContactPersonService } from 'app/entities/contact-person/service/contact-person.service';
import { ILayout } from 'app/entities/layout/layout.model';
import { LayoutService } from 'app/entities/layout/service/layout.service';
import { ISignature } from 'app/entities/signature/signature.model';
import { SignatureService } from 'app/entities/signature/service/signature.service';

@Component({
  selector: 'fa-document-letter-update',
  templateUrl: './document-letter-update.component.html',
})
export class DocumentLetterUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];
  contactAddressesSharedCollection: IContactAddress[] = [];
  contactPeopleSharedCollection: IContactPerson[] = [];
  layoutsSharedCollection: ILayout[] = [];
  signaturesSharedCollection: ISignature[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    contactName: [],
    date: [],
    title: [],
    content: [],
    language: [],
    pageAmount: [],
    notes: [],
    status: [],
    created: [],
    contact: [],
    contactAddress: [],
    contactPerson: [],
    contactPrePageAddress: [],
    layout: [],
    signature: [],
  });

  constructor(
    protected documentLetterService: DocumentLetterService,
    protected contactService: ContactService,
    protected contactAddressService: ContactAddressService,
    protected contactPersonService: ContactPersonService,
    protected layoutService: LayoutService,
    protected signatureService: SignatureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentLetter }) => {
      if (documentLetter.id === undefined) {
        const today = dayjs().startOf('day');
        documentLetter.created = today;
      }

      this.updateForm(documentLetter);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentLetter = this.createFromForm();
    if (documentLetter.id !== undefined) {
      this.subscribeToSaveResponse(this.documentLetterService.update(documentLetter));
    } else {
      this.subscribeToSaveResponse(this.documentLetterService.create(documentLetter));
    }
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  trackContactAddressById(index: number, item: IContactAddress): number {
    return item.id!;
  }

  trackContactPersonById(index: number, item: IContactPerson): number {
    return item.id!;
  }

  trackLayoutById(index: number, item: ILayout): number {
    return item.id!;
  }

  trackSignatureById(index: number, item: ISignature): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentLetter>>): void {
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

  protected updateForm(documentLetter: IDocumentLetter): void {
    this.editForm.patchValue({
      id: documentLetter.id,
      remoteId: documentLetter.remoteId,
      contactName: documentLetter.contactName,
      date: documentLetter.date,
      title: documentLetter.title,
      content: documentLetter.content,
      language: documentLetter.language,
      pageAmount: documentLetter.pageAmount,
      notes: documentLetter.notes,
      status: documentLetter.status,
      created: documentLetter.created ? documentLetter.created.format(DATE_TIME_FORMAT) : null,
      contact: documentLetter.contact,
      contactAddress: documentLetter.contactAddress,
      contactPerson: documentLetter.contactPerson,
      contactPrePageAddress: documentLetter.contactPrePageAddress,
      layout: documentLetter.layout,
      signature: documentLetter.signature,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      documentLetter.contact
    );
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      documentLetter.contactAddress,
      documentLetter.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      documentLetter.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutToCollectionIfMissing(this.layoutsSharedCollection, documentLetter.layout);
    this.signaturesSharedCollection = this.signatureService.addSignatureToCollectionIfMissing(
      this.signaturesSharedCollection,
      documentLetter.signature
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContact[]) => this.contactService.addContactToCollectionIfMissing(contacts, this.editForm.get('contact')!.value))
      )
      .subscribe((contacts: IContact[]) => (this.contactsSharedCollection = contacts));

    this.contactAddressService
      .query()
      .pipe(map((res: HttpResponse<IContactAddress[]>) => res.body ?? []))
      .pipe(
        map((contactAddresses: IContactAddress[]) =>
          this.contactAddressService.addContactAddressToCollectionIfMissing(
            contactAddresses,
            this.editForm.get('contactAddress')!.value,
            this.editForm.get('contactPrePageAddress')!.value
          )
        )
      )
      .subscribe((contactAddresses: IContactAddress[]) => (this.contactAddressesSharedCollection = contactAddresses));

    this.contactPersonService
      .query()
      .pipe(map((res: HttpResponse<IContactPerson[]>) => res.body ?? []))
      .pipe(
        map((contactPeople: IContactPerson[]) =>
          this.contactPersonService.addContactPersonToCollectionIfMissing(contactPeople, this.editForm.get('contactPerson')!.value)
        )
      )
      .subscribe((contactPeople: IContactPerson[]) => (this.contactPeopleSharedCollection = contactPeople));

    this.layoutService
      .query()
      .pipe(map((res: HttpResponse<ILayout[]>) => res.body ?? []))
      .pipe(map((layouts: ILayout[]) => this.layoutService.addLayoutToCollectionIfMissing(layouts, this.editForm.get('layout')!.value)))
      .subscribe((layouts: ILayout[]) => (this.layoutsSharedCollection = layouts));

    this.signatureService
      .query()
      .pipe(map((res: HttpResponse<ISignature[]>) => res.body ?? []))
      .pipe(
        map((signatures: ISignature[]) =>
          this.signatureService.addSignatureToCollectionIfMissing(signatures, this.editForm.get('signature')!.value)
        )
      )
      .subscribe((signatures: ISignature[]) => (this.signaturesSharedCollection = signatures));
  }

  protected createFromForm(): IDocumentLetter {
    return {
      ...new DocumentLetter(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      date: this.editForm.get(['date'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      language: this.editForm.get(['language'])!.value,
      pageAmount: this.editForm.get(['pageAmount'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      status: this.editForm.get(['status'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      contact: this.editForm.get(['contact'])!.value,
      contactAddress: this.editForm.get(['contactAddress'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
      contactPrePageAddress: this.editForm.get(['contactPrePageAddress'])!.value,
      layout: this.editForm.get(['layout'])!.value,
      signature: this.editForm.get(['signature'])!.value,
    };
  }
}
