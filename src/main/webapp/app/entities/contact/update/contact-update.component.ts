import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContact, Contact } from '../contact.model';
import { ContactService } from '../service/contact.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IContactRelation } from 'app/entities/contact-relation/contact-relation.model';
import { ContactRelationService } from 'app/entities/contact-relation/service/contact-relation.service';
import { IContactGroup } from 'app/entities/contact-group/contact-group.model';
import { ContactGroupService } from 'app/entities/contact-group/service/contact-group.service';

@Component({
  selector: 'fa-contact-update',
  templateUrl: './contact-update.component.html',
})
export class ContactUpdateComponent implements OnInit {
  isSaving = false;

  contactRelationsSharedCollection: IContactRelation[] = [];
  contactGroupsSharedCollection: IContactGroup[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [null, []],
    type: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    genderSalutationActive: [null, [Validators.required]],
    name: [null, [Validators.required]],
    nameAddition: [],
    salutation: [],
    phone: [],
    fax: [],
    email: [],
    website: [],
    notes: [],
    communicationLanguage: [],
    communicationChannel: [],
    communicationNewsletter: [],
    currency: [],
    ebillAccountId: [],
    vatIdentification: [],
    vatRate: [],
    discountRate: [],
    discountType: [],
    paymentGrace: [],
    hourlyRate: [],
    created: [],
    mainAddressId: [],
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
    relations: [],
    groups: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected contactService: ContactService,
    protected contactRelationService: ContactRelationService,
    protected contactGroupService: ContactGroupService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contact }) => {
      if (contact.id === undefined) {
        const today = dayjs().startOf('day');
        contact.created = today;
      }

      this.updateForm(contact);

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
    const contact = this.createFromForm();
    if (contact.id !== undefined) {
      this.subscribeToSaveResponse(this.contactService.update(contact));
    } else {
      this.subscribeToSaveResponse(this.contactService.create(contact));
    }
  }

  trackContactRelationById(index: number, item: IContactRelation): number {
    return item.id!;
  }

  trackContactGroupById(index: number, item: IContactGroup): number {
    return item.id!;
  }

  getSelectedContactRelation(option: IContactRelation, selectedVals?: IContactRelation[]): IContactRelation {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedContactGroup(option: IContactGroup, selectedVals?: IContactGroup[]): IContactGroup {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContact>>): void {
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

  protected updateForm(contact: IContact): void {
    this.editForm.patchValue({
      id: contact.id,
      remoteId: contact.remoteId,
      number: contact.number,
      type: contact.type,
      gender: contact.gender,
      genderSalutationActive: contact.genderSalutationActive,
      name: contact.name,
      nameAddition: contact.nameAddition,
      salutation: contact.salutation,
      phone: contact.phone,
      fax: contact.fax,
      email: contact.email,
      website: contact.website,
      notes: contact.notes,
      communicationLanguage: contact.communicationLanguage,
      communicationChannel: contact.communicationChannel,
      communicationNewsletter: contact.communicationNewsletter,
      currency: contact.currency,
      ebillAccountId: contact.ebillAccountId,
      vatIdentification: contact.vatIdentification,
      vatRate: contact.vatRate,
      discountRate: contact.discountRate,
      discountType: contact.discountType,
      paymentGrace: contact.paymentGrace,
      hourlyRate: contact.hourlyRate,
      created: contact.created ? contact.created.format(DATE_TIME_FORMAT) : null,
      mainAddressId: contact.mainAddressId,
      birthDate: contact.birthDate,
      birthPlace: contact.birthPlace,
      placeOfOrigin: contact.placeOfOrigin,
      citizenCountry1: contact.citizenCountry1,
      citizenCountry2: contact.citizenCountry2,
      socialSecurityNumber: contact.socialSecurityNumber,
      hobbies: contact.hobbies,
      dailyWork: contact.dailyWork,
      contactAttribute01: contact.contactAttribute01,
      avatar: contact.avatar,
      avatarContentType: contact.avatarContentType,
      imageType: contact.imageType,
      inactiv: contact.inactiv,
      relations: contact.relations,
      groups: contact.groups,
    });

    this.contactRelationsSharedCollection = this.contactRelationService.addContactRelationToCollectionIfMissing(
      this.contactRelationsSharedCollection,
      ...(contact.relations ?? [])
    );
    this.contactGroupsSharedCollection = this.contactGroupService.addContactGroupToCollectionIfMissing(
      this.contactGroupsSharedCollection,
      ...(contact.groups ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactRelationService
      .query()
      .pipe(map((res: HttpResponse<IContactRelation[]>) => res.body ?? []))
      .pipe(
        map((contactRelations: IContactRelation[]) =>
          this.contactRelationService.addContactRelationToCollectionIfMissing(
            contactRelations,
            ...(this.editForm.get('relations')!.value ?? [])
          )
        )
      )
      .subscribe((contactRelations: IContactRelation[]) => (this.contactRelationsSharedCollection = contactRelations));

    this.contactGroupService
      .query()
      .pipe(map((res: HttpResponse<IContactGroup[]>) => res.body ?? []))
      .pipe(
        map((contactGroups: IContactGroup[]) =>
          this.contactGroupService.addContactGroupToCollectionIfMissing(contactGroups, ...(this.editForm.get('groups')!.value ?? []))
        )
      )
      .subscribe((contactGroups: IContactGroup[]) => (this.contactGroupsSharedCollection = contactGroups));
  }

  protected createFromForm(): IContact {
    return {
      ...new Contact(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      type: this.editForm.get(['type'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      genderSalutationActive: this.editForm.get(['genderSalutationActive'])!.value,
      name: this.editForm.get(['name'])!.value,
      nameAddition: this.editForm.get(['nameAddition'])!.value,
      salutation: this.editForm.get(['salutation'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      fax: this.editForm.get(['fax'])!.value,
      email: this.editForm.get(['email'])!.value,
      website: this.editForm.get(['website'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      communicationLanguage: this.editForm.get(['communicationLanguage'])!.value,
      communicationChannel: this.editForm.get(['communicationChannel'])!.value,
      communicationNewsletter: this.editForm.get(['communicationNewsletter'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      ebillAccountId: this.editForm.get(['ebillAccountId'])!.value,
      vatIdentification: this.editForm.get(['vatIdentification'])!.value,
      vatRate: this.editForm.get(['vatRate'])!.value,
      discountRate: this.editForm.get(['discountRate'])!.value,
      discountType: this.editForm.get(['discountType'])!.value,
      paymentGrace: this.editForm.get(['paymentGrace'])!.value,
      hourlyRate: this.editForm.get(['hourlyRate'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      mainAddressId: this.editForm.get(['mainAddressId'])!.value,
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
      relations: this.editForm.get(['relations'])!.value,
      groups: this.editForm.get(['groups'])!.value,
    };
  }
}
