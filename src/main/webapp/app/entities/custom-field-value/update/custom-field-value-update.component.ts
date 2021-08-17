import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICustomFieldValue, CustomFieldValue } from '../custom-field-value.model';
import { CustomFieldValueService } from '../service/custom-field-value.service';
import { ICustomField } from 'app/entities/custom-field/custom-field.model';
import { CustomFieldService } from 'app/entities/custom-field/service/custom-field.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ContactPersonService } from 'app/entities/contact-person/service/contact-person.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { ICatalogProduct } from 'app/entities/catalog-product/catalog-product.model';
import { CatalogProductService } from 'app/entities/catalog-product/service/catalog-product.service';
import { ICatalogService } from 'app/entities/catalog-service/catalog-service.model';
import { CatalogServiceService } from 'app/entities/catalog-service/service/catalog-service.service';
import { IDocumentLetter } from 'app/entities/document-letter/document-letter.model';
import { DocumentLetterService } from 'app/entities/document-letter/service/document-letter.service';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';

@Component({
  selector: 'fa-custom-field-value-update',
  templateUrl: './custom-field-value-update.component.html',
})
export class CustomFieldValueUpdateComponent implements OnInit {
  isSaving = false;

  customFieldsSharedCollection: ICustomField[] = [];
  contactsSharedCollection: IContact[] = [];
  contactPeopleSharedCollection: IContactPerson[] = [];
  projectsSharedCollection: IProject[] = [];
  catalogProductsSharedCollection: ICatalogProduct[] = [];
  catalogServicesSharedCollection: ICatalogService[] = [];
  documentLettersSharedCollection: IDocumentLetter[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    value: [],
    customField: [],
    contact: [],
    contactPerson: [],
    project: [],
    catalogProduct: [],
    catalogService: [],
    documentLetter: [],
    deliveryNote: [],
  });

  constructor(
    protected customFieldValueService: CustomFieldValueService,
    protected customFieldService: CustomFieldService,
    protected contactService: ContactService,
    protected contactPersonService: ContactPersonService,
    protected projectService: ProjectService,
    protected catalogProductService: CatalogProductService,
    protected catalogServiceService: CatalogServiceService,
    protected documentLetterService: DocumentLetterService,
    protected deliveryNoteService: DeliveryNoteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customFieldValue }) => {
      this.updateForm(customFieldValue);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customFieldValue = this.createFromForm();
    if (customFieldValue.id !== undefined) {
      this.subscribeToSaveResponse(this.customFieldValueService.update(customFieldValue));
    } else {
      this.subscribeToSaveResponse(this.customFieldValueService.create(customFieldValue));
    }
  }

  trackCustomFieldById(index: number, item: ICustomField): number {
    return item.id!;
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  trackContactPersonById(index: number, item: IContactPerson): number {
    return item.id!;
  }

  trackProjectById(index: number, item: IProject): number {
    return item.id!;
  }

  trackCatalogProductById(index: number, item: ICatalogProduct): number {
    return item.id!;
  }

  trackCatalogServiceById(index: number, item: ICatalogService): number {
    return item.id!;
  }

  trackDocumentLetterById(index: number, item: IDocumentLetter): number {
    return item.id!;
  }

  trackDeliveryNoteById(index: number, item: IDeliveryNote): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomFieldValue>>): void {
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

  protected updateForm(customFieldValue: ICustomFieldValue): void {
    this.editForm.patchValue({
      id: customFieldValue.id,
      key: customFieldValue.key,
      name: customFieldValue.name,
      value: customFieldValue.value,
      customField: customFieldValue.customField,
      contact: customFieldValue.contact,
      contactPerson: customFieldValue.contactPerson,
      project: customFieldValue.project,
      catalogProduct: customFieldValue.catalogProduct,
      catalogService: customFieldValue.catalogService,
      documentLetter: customFieldValue.documentLetter,
      deliveryNote: customFieldValue.deliveryNote,
    });

    this.customFieldsSharedCollection = this.customFieldService.addCustomFieldToCollectionIfMissing(
      this.customFieldsSharedCollection,
      customFieldValue.customField
    );
    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      customFieldValue.contact
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      customFieldValue.contactPerson
    );
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing(
      this.projectsSharedCollection,
      customFieldValue.project
    );
    this.catalogProductsSharedCollection = this.catalogProductService.addCatalogProductToCollectionIfMissing(
      this.catalogProductsSharedCollection,
      customFieldValue.catalogProduct
    );
    this.catalogServicesSharedCollection = this.catalogServiceService.addCatalogServiceToCollectionIfMissing(
      this.catalogServicesSharedCollection,
      customFieldValue.catalogService
    );
    this.documentLettersSharedCollection = this.documentLetterService.addDocumentLetterToCollectionIfMissing(
      this.documentLettersSharedCollection,
      customFieldValue.documentLetter
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      customFieldValue.deliveryNote
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customFieldService
      .query()
      .pipe(map((res: HttpResponse<ICustomField[]>) => res.body ?? []))
      .pipe(
        map((customFields: ICustomField[]) =>
          this.customFieldService.addCustomFieldToCollectionIfMissing(customFields, this.editForm.get('customField')!.value)
        )
      )
      .subscribe((customFields: ICustomField[]) => (this.customFieldsSharedCollection = customFields));

    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContact[]) => this.contactService.addContactToCollectionIfMissing(contacts, this.editForm.get('contact')!.value))
      )
      .subscribe((contacts: IContact[]) => (this.contactsSharedCollection = contacts));

    this.contactPersonService
      .query()
      .pipe(map((res: HttpResponse<IContactPerson[]>) => res.body ?? []))
      .pipe(
        map((contactPeople: IContactPerson[]) =>
          this.contactPersonService.addContactPersonToCollectionIfMissing(contactPeople, this.editForm.get('contactPerson')!.value)
        )
      )
      .subscribe((contactPeople: IContactPerson[]) => (this.contactPeopleSharedCollection = contactPeople));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing(projects, this.editForm.get('project')!.value))
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));

    this.catalogProductService
      .query()
      .pipe(map((res: HttpResponse<ICatalogProduct[]>) => res.body ?? []))
      .pipe(
        map((catalogProducts: ICatalogProduct[]) =>
          this.catalogProductService.addCatalogProductToCollectionIfMissing(catalogProducts, this.editForm.get('catalogProduct')!.value)
        )
      )
      .subscribe((catalogProducts: ICatalogProduct[]) => (this.catalogProductsSharedCollection = catalogProducts));

    this.catalogServiceService
      .query()
      .pipe(map((res: HttpResponse<ICatalogService[]>) => res.body ?? []))
      .pipe(
        map((catalogServices: ICatalogService[]) =>
          this.catalogServiceService.addCatalogServiceToCollectionIfMissing(catalogServices, this.editForm.get('catalogService')!.value)
        )
      )
      .subscribe((catalogServices: ICatalogService[]) => (this.catalogServicesSharedCollection = catalogServices));

    this.documentLetterService
      .query()
      .pipe(map((res: HttpResponse<IDocumentLetter[]>) => res.body ?? []))
      .pipe(
        map((documentLetters: IDocumentLetter[]) =>
          this.documentLetterService.addDocumentLetterToCollectionIfMissing(documentLetters, this.editForm.get('documentLetter')!.value)
        )
      )
      .subscribe((documentLetters: IDocumentLetter[]) => (this.documentLettersSharedCollection = documentLetters));

    this.deliveryNoteService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryNote[]>) => res.body ?? []))
      .pipe(
        map((deliveryNotes: IDeliveryNote[]) =>
          this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(deliveryNotes, this.editForm.get('deliveryNote')!.value)
        )
      )
      .subscribe((deliveryNotes: IDeliveryNote[]) => (this.deliveryNotesSharedCollection = deliveryNotes));
  }

  protected createFromForm(): ICustomFieldValue {
    return {
      ...new CustomFieldValue(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      value: this.editForm.get(['value'])!.value,
      customField: this.editForm.get(['customField'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
      project: this.editForm.get(['project'])!.value,
      catalogProduct: this.editForm.get(['catalogProduct'])!.value,
      catalogService: this.editForm.get(['catalogService'])!.value,
      documentLetter: this.editForm.get(['documentLetter'])!.value,
      deliveryNote: this.editForm.get(['deliveryNote'])!.value,
    };
  }
}
