import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContactFa, ContactFa } from '../contact-fa.model';
import { ContactFaService } from '../service/contact-fa.service';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { ContactAddressFaService } from 'app/entities/contact-address-fa/service/contact-address-fa.service';
import { IContactRelationFa } from 'app/entities/contact-relation-fa/contact-relation-fa.model';
import { ContactRelationFaService } from 'app/entities/contact-relation-fa/service/contact-relation-fa.service';

@Component({
  selector: 'fa-contact-fa-update',
  templateUrl: './contact-fa-update.component.html',
})
export class ContactFaUpdateComponent implements OnInit {
  isSaving = false;

  contactMainAddressesCollection: IContactAddressFa[] = [];
  contactRelationsSharedCollection: IContactRelationFa[] = [];

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
    contactMainAddress: [],
    relations: [],
  });

  constructor(
    protected contactService: ContactFaService,
    protected contactAddressService: ContactAddressFaService,
    protected contactRelationService: ContactRelationFaService,
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

  trackContactAddressFaById(index: number, item: IContactAddressFa): number {
    return item.id!;
  }

  trackContactRelationFaById(index: number, item: IContactRelationFa): number {
    return item.id!;
  }

  getSelectedContactRelationFa(option: IContactRelationFa, selectedVals?: IContactRelationFa[]): IContactRelationFa {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactFa>>): void {
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

  protected updateForm(contact: IContactFa): void {
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
      contactMainAddress: contact.contactMainAddress,
      relations: contact.relations,
    });

    this.contactMainAddressesCollection = this.contactAddressService.addContactAddressFaToCollectionIfMissing(
      this.contactMainAddressesCollection,
      contact.contactMainAddress
    );
    this.contactRelationsSharedCollection = this.contactRelationService.addContactRelationFaToCollectionIfMissing(
      this.contactRelationsSharedCollection,
      ...(contact.relations ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactAddressService
      .query({ filter: 'contact-is-null' })
      .pipe(map((res: HttpResponse<IContactAddressFa[]>) => res.body ?? []))
      .pipe(
        map((contactAddresses: IContactAddressFa[]) =>
          this.contactAddressService.addContactAddressFaToCollectionIfMissing(
            contactAddresses,
            this.editForm.get('contactMainAddress')!.value
          )
        )
      )
      .subscribe((contactAddresses: IContactAddressFa[]) => (this.contactMainAddressesCollection = contactAddresses));

    this.contactRelationService
      .query()
      .pipe(map((res: HttpResponse<IContactRelationFa[]>) => res.body ?? []))
      .pipe(
        map((contactRelations: IContactRelationFa[]) =>
          this.contactRelationService.addContactRelationFaToCollectionIfMissing(
            contactRelations,
            ...(this.editForm.get('relations')!.value ?? [])
          )
        )
      )
      .subscribe((contactRelations: IContactRelationFa[]) => (this.contactRelationsSharedCollection = contactRelations));
  }

  protected createFromForm(): IContactFa {
    return {
      ...new ContactFa(),
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
      contactMainAddress: this.editForm.get(['contactMainAddress'])!.value,
      relations: this.editForm.get(['relations'])!.value,
    };
  }
}
