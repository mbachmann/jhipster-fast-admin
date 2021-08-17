jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomFieldValueService } from '../service/custom-field-value.service';
import { ICustomFieldValue, CustomFieldValue } from '../custom-field-value.model';
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

import { CustomFieldValueUpdateComponent } from './custom-field-value-update.component';

describe('Component Tests', () => {
  describe('CustomFieldValue Management Update Component', () => {
    let comp: CustomFieldValueUpdateComponent;
    let fixture: ComponentFixture<CustomFieldValueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let customFieldValueService: CustomFieldValueService;
    let customFieldService: CustomFieldService;
    let contactService: ContactService;
    let contactPersonService: ContactPersonService;
    let projectService: ProjectService;
    let catalogProductService: CatalogProductService;
    let catalogServiceService: CatalogServiceService;
    let documentLetterService: DocumentLetterService;
    let deliveryNoteService: DeliveryNoteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldValueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CustomFieldValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldValueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      customFieldValueService = TestBed.inject(CustomFieldValueService);
      customFieldService = TestBed.inject(CustomFieldService);
      contactService = TestBed.inject(ContactService);
      contactPersonService = TestBed.inject(ContactPersonService);
      projectService = TestBed.inject(ProjectService);
      catalogProductService = TestBed.inject(CatalogProductService);
      catalogServiceService = TestBed.inject(CatalogServiceService);
      documentLetterService = TestBed.inject(DocumentLetterService);
      deliveryNoteService = TestBed.inject(DeliveryNoteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CustomField query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const customField: ICustomField = { id: 66888 };
        customFieldValue.customField = customField;

        const customFieldCollection: ICustomField[] = [{ id: 82665 }];
        jest.spyOn(customFieldService, 'query').mockReturnValue(of(new HttpResponse({ body: customFieldCollection })));
        const additionalCustomFields = [customField];
        const expectedCollection: ICustomField[] = [...additionalCustomFields, ...customFieldCollection];
        jest.spyOn(customFieldService, 'addCustomFieldToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(customFieldService.query).toHaveBeenCalled();
        expect(customFieldService.addCustomFieldToCollectionIfMissing).toHaveBeenCalledWith(
          customFieldCollection,
          ...additionalCustomFields
        );
        expect(comp.customFieldsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Contact query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const contact: IContact = { id: 14154 };
        customFieldValue.contact = contact;

        const contactCollection: IContact[] = [{ id: 32799 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPerson query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const contactPerson: IContactPerson = { id: 3278 };
        customFieldValue.contactPerson = contactPerson;

        const contactPersonCollection: IContactPerson[] = [{ id: 1379 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPeople = [contactPerson];
        const expectedCollection: IContactPerson[] = [...additionalContactPeople, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPeople
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Project query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const project: IProject = { id: 8775 };
        customFieldValue.project = project;

        const projectCollection: IProject[] = [{ id: 48744 }];
        jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
        const additionalProjects = [project];
        const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
        jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(projectService.query).toHaveBeenCalled();
        expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(projectCollection, ...additionalProjects);
        expect(comp.projectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CatalogProduct query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const catalogProduct: ICatalogProduct = { id: 87543 };
        customFieldValue.catalogProduct = catalogProduct;

        const catalogProductCollection: ICatalogProduct[] = [{ id: 314 }];
        jest.spyOn(catalogProductService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogProductCollection })));
        const additionalCatalogProducts = [catalogProduct];
        const expectedCollection: ICatalogProduct[] = [...additionalCatalogProducts, ...catalogProductCollection];
        jest.spyOn(catalogProductService, 'addCatalogProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(catalogProductService.query).toHaveBeenCalled();
        expect(catalogProductService.addCatalogProductToCollectionIfMissing).toHaveBeenCalledWith(
          catalogProductCollection,
          ...additionalCatalogProducts
        );
        expect(comp.catalogProductsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CatalogService query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const catalogService: ICatalogService = { id: 4617 };
        customFieldValue.catalogService = catalogService;

        const catalogServiceCollection: ICatalogService[] = [{ id: 45917 }];
        jest.spyOn(catalogServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogServiceCollection })));
        const additionalCatalogServices = [catalogService];
        const expectedCollection: ICatalogService[] = [...additionalCatalogServices, ...catalogServiceCollection];
        jest.spyOn(catalogServiceService, 'addCatalogServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(catalogServiceService.query).toHaveBeenCalled();
        expect(catalogServiceService.addCatalogServiceToCollectionIfMissing).toHaveBeenCalledWith(
          catalogServiceCollection,
          ...additionalCatalogServices
        );
        expect(comp.catalogServicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DocumentLetter query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const documentLetter: IDocumentLetter = { id: 24233 };
        customFieldValue.documentLetter = documentLetter;

        const documentLetterCollection: IDocumentLetter[] = [{ id: 10113 }];
        jest.spyOn(documentLetterService, 'query').mockReturnValue(of(new HttpResponse({ body: documentLetterCollection })));
        const additionalDocumentLetters = [documentLetter];
        const expectedCollection: IDocumentLetter[] = [...additionalDocumentLetters, ...documentLetterCollection];
        jest.spyOn(documentLetterService, 'addDocumentLetterToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(documentLetterService.query).toHaveBeenCalled();
        expect(documentLetterService.addDocumentLetterToCollectionIfMissing).toHaveBeenCalledWith(
          documentLetterCollection,
          ...additionalDocumentLetters
        );
        expect(comp.documentLettersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DeliveryNote query and add missing value', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const deliveryNote: IDeliveryNote = { id: 78562 };
        customFieldValue.deliveryNote = deliveryNote;

        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 48822 }];
        jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
        const additionalDeliveryNotes = [deliveryNote];
        const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
        jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(deliveryNoteService.query).toHaveBeenCalled();
        expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
          deliveryNoteCollection,
          ...additionalDeliveryNotes
        );
        expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const customFieldValue: ICustomFieldValue = { id: 456 };
        const customField: ICustomField = { id: 81074 };
        customFieldValue.customField = customField;
        const contact: IContact = { id: 84772 };
        customFieldValue.contact = contact;
        const contactPerson: IContactPerson = { id: 88121 };
        customFieldValue.contactPerson = contactPerson;
        const project: IProject = { id: 38210 };
        customFieldValue.project = project;
        const catalogProduct: ICatalogProduct = { id: 73783 };
        customFieldValue.catalogProduct = catalogProduct;
        const catalogService: ICatalogService = { id: 8349 };
        customFieldValue.catalogService = catalogService;
        const documentLetter: IDocumentLetter = { id: 57740 };
        customFieldValue.documentLetter = documentLetter;
        const deliveryNote: IDeliveryNote = { id: 3138 };
        customFieldValue.deliveryNote = deliveryNote;

        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(customFieldValue));
        expect(comp.customFieldsSharedCollection).toContain(customField);
        expect(comp.contactsSharedCollection).toContain(contact);
        expect(comp.contactPeopleSharedCollection).toContain(contactPerson);
        expect(comp.projectsSharedCollection).toContain(project);
        expect(comp.catalogProductsSharedCollection).toContain(catalogProduct);
        expect(comp.catalogServicesSharedCollection).toContain(catalogService);
        expect(comp.documentLettersSharedCollection).toContain(documentLetter);
        expect(comp.deliveryNotesSharedCollection).toContain(deliveryNote);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomFieldValue>>();
        const customFieldValue = { id: 123 };
        jest.spyOn(customFieldValueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customFieldValue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(customFieldValueService.update).toHaveBeenCalledWith(customFieldValue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomFieldValue>>();
        const customFieldValue = new CustomFieldValue();
        jest.spyOn(customFieldValueService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customFieldValue }));
        saveSubject.complete();

        // THEN
        expect(customFieldValueService.create).toHaveBeenCalledWith(customFieldValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomFieldValue>>();
        const customFieldValue = { id: 123 };
        jest.spyOn(customFieldValueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customFieldValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(customFieldValueService.update).toHaveBeenCalledWith(customFieldValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCustomFieldById', () => {
        it('Should return tracked CustomField primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCustomFieldById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactById', () => {
        it('Should return tracked Contact primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactPersonById', () => {
        it('Should return tracked ContactPerson primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProjectById', () => {
        it('Should return tracked Project primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProjectById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCatalogProductById', () => {
        it('Should return tracked CatalogProduct primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCatalogServiceById', () => {
        it('Should return tracked CatalogService primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogServiceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDocumentLetterById', () => {
        it('Should return tracked DocumentLetter primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentLetterById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeliveryNoteById', () => {
        it('Should return tracked DeliveryNote primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeliveryNoteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
