import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactPerson, ContactPerson } from '../contact-person.model';
import { ContactPersonService } from '../service/contact-person.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'fa-contact-person-update',
  templateUrl: './contact-person-update.component.html',
})
export class ContactPersonUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    defaultPerson: [],
    name: [],
    surname: [],
    gender: [],
    email: [],
    phone: [],
    department: [],
    salutation: [],
    showTitle: [],
    showDepartment: [],
    wantsNewsletter: [],
    birthDate: [],
    birthPlace: [],
    placeOfOrigin: [],
    citizenCountry1: [],
    citizenCountry2: [],
    socialSecurityNumber: [],
    hobbies: [],
    dailyWork: [],
    contactAttribute01: [],
    avatar: [],
    avatarContentType: [],
    imageType: [],
    inactiv: [],
    contact: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected contactPersonService: ContactPersonService,
    protected contactService: ContactService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactPerson }) => {
      this.updateForm(contactPerson);

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
    const contactPerson = this.createFromForm();
    if (contactPerson.id !== undefined) {
      this.subscribeToSaveResponse(this.contactPersonService.update(contactPerson));
    } else {
      this.subscribeToSaveResponse(this.contactPersonService.create(contactPerson));
    }
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactPerson>>): void {
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

  protected updateForm(contactPerson: IContactPerson): void {
    this.editForm.patchValue({
      id: contactPerson.id,
      remoteId: contactPerson.remoteId,
      defaultPerson: contactPerson.defaultPerson,
      name: contactPerson.name,
      surname: contactPerson.surname,
      gender: contactPerson.gender,
      email: contactPerson.email,
      phone: contactPerson.phone,
      department: contactPerson.department,
      salutation: contactPerson.salutation,
      showTitle: contactPerson.showTitle,
      showDepartment: contactPerson.showDepartment,
      wantsNewsletter: contactPerson.wantsNewsletter,
      birthDate: contactPerson.birthDate,
      birthPlace: contactPerson.birthPlace,
      placeOfOrigin: contactPerson.placeOfOrigin,
      citizenCountry1: contactPerson.citizenCountry1,
      citizenCountry2: contactPerson.citizenCountry2,
      socialSecurityNumber: contactPerson.socialSecurityNumber,
      hobbies: contactPerson.hobbies,
      dailyWork: contactPerson.dailyWork,
      contactAttribute01: contactPerson.contactAttribute01,
      avatar: contactPerson.avatar,
      avatarContentType: contactPerson.avatarContentType,
      imageType: contactPerson.imageType,
      inactiv: contactPerson.inactiv,
      contact: contactPerson.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      contactPerson.contact
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
  }

  protected createFromForm(): IContactPerson {
    return {
      ...new ContactPerson(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      defaultPerson: this.editForm.get(['defaultPerson'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      department: this.editForm.get(['department'])!.value,
      salutation: this.editForm.get(['salutation'])!.value,
      showTitle: this.editForm.get(['showTitle'])!.value,
      showDepartment: this.editForm.get(['showDepartment'])!.value,
      wantsNewsletter: this.editForm.get(['wantsNewsletter'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      birthPlace: this.editForm.get(['birthPlace'])!.value,
      placeOfOrigin: this.editForm.get(['placeOfOrigin'])!.value,
      citizenCountry1: this.editForm.get(['citizenCountry1'])!.value,
      citizenCountry2: this.editForm.get(['citizenCountry2'])!.value,
      socialSecurityNumber: this.editForm.get(['socialSecurityNumber'])!.value,
      hobbies: this.editForm.get(['hobbies'])!.value,
      dailyWork: this.editForm.get(['dailyWork'])!.value,
      contactAttribute01: this.editForm.get(['contactAttribute01'])!.value,
      avatarContentType: this.editForm.get(['avatarContentType'])!.value,
      avatar: this.editForm.get(['avatar'])!.value,
      imageType: this.editForm.get(['imageType'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
