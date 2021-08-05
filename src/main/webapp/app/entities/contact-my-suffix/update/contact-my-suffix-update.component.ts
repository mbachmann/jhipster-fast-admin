import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContactMySuffix, ContactMySuffix } from '../contact-my-suffix.model';
import { ContactMySuffixService } from '../service/contact-my-suffix.service';
import { IContactAddressMySuffix } from 'app/entities/contact-address-my-suffix/contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from 'app/entities/contact-address-my-suffix/service/contact-address-my-suffix.service';
import { IContactRelationMySuffix } from 'app/entities/contact-relation-my-suffix/contact-relation-my-suffix.model';
import { ContactRelationMySuffixService } from 'app/entities/contact-relation-my-suffix/service/contact-relation-my-suffix.service';

@Component({
  selector: 'jhl-contact-my-suffix-update',
  templateUrl: './contact-my-suffix-update.component.html',
})
export class ContactMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  mainAddressesCollection: IContactAddressMySuffix[] = [];
  contactRelationsSharedCollection: IContactRelationMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [null, [Validators.required]],
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
    mainAddress: [],
    relations: [],
  });

  constructor(
    protected contactService: ContactMySuffixService,
    protected contactAddressService: ContactAddressMySuffixService,
    protected contactRelationService: ContactRelationMySuffixService,
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

  trackContactAddressMySuffixById(index: number, item: IContactAddressMySuffix): number {
    return item.id!;
  }

  trackContactRelationMySuffixById(index: number, item: IContactRelationMySuffix): number {
    return item.id!;
  }

  getSelectedContactRelationMySuffix(
    option: IContactRelationMySuffix,
    selectedVals?: IContactRelationMySuffix[]
  ): IContactRelationMySuffix {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactMySuffix>>): void {
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

  protected updateForm(contact: IContactMySuffix): void {
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
      mainAddress: contact.mainAddress,
      relations: contact.relations,
    });

    this.mainAddressesCollection = this.contactAddressService.addContactAddressMySuffixToCollectionIfMissing(
      this.mainAddressesCollection,
      contact.mainAddress
    );
    this.contactRelationsSharedCollection = this.contactRelationService.addContactRelationMySuffixToCollectionIfMissing(
      this.contactRelationsSharedCollection,
      ...(contact.relations ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactAddressService
      .query({ 'contactId.specified': 'false' })
      .pipe(map((res: HttpResponse<IContactAddressMySuffix[]>) => res.body ?? []))
      .pipe(
        map((contactAddresses: IContactAddressMySuffix[]) =>
          this.contactAddressService.addContactAddressMySuffixToCollectionIfMissing(
            contactAddresses,
            this.editForm.get('mainAddress')!.value
          )
        )
      )
      .subscribe((contactAddresses: IContactAddressMySuffix[]) => (this.mainAddressesCollection = contactAddresses));

    this.contactRelationService
      .query()
      .pipe(map((res: HttpResponse<IContactRelationMySuffix[]>) => res.body ?? []))
      .pipe(
        map((contactRelations: IContactRelationMySuffix[]) =>
          this.contactRelationService.addContactRelationMySuffixToCollectionIfMissing(
            contactRelations,
            ...(this.editForm.get('relations')!.value ?? [])
          )
        )
      )
      .subscribe((contactRelations: IContactRelationMySuffix[]) => (this.contactRelationsSharedCollection = contactRelations));
  }

  protected createFromForm(): IContactMySuffix {
    return {
      ...new ContactMySuffix(),
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
      mainAddress: this.editForm.get(['mainAddress'])!.value,
      relations: this.editForm.get(['relations'])!.value,
    };
  }
}
