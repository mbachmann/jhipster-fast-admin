import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactGroupFa, ContactGroupFa } from '../contact-group-fa.model';

import { ContactGroupFaService } from './contact-group-fa.service';

describe('Service Tests', () => {
  describe('ContactGroupFa Service', () => {
    let service: ContactGroupFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactGroupFa;
    let expectedResult: IContactGroupFa | IContactGroupFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactGroupFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContactGroupFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactGroupFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactGroupFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactGroupFa', () => {
        const patchObject = Object.assign({}, new ContactGroupFa());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactGroupFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContactGroupFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactGroupFaToCollectionIfMissing', () => {
        it('should add a ContactGroupFa to an empty array', () => {
          const contactGroup: IContactGroupFa = { id: 123 };
          expectedResult = service.addContactGroupFaToCollectionIfMissing([], contactGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should not add a ContactGroupFa to an array that contains it', () => {
          const contactGroup: IContactGroupFa = { id: 123 };
          const contactGroupCollection: IContactGroupFa[] = [
            {
              ...contactGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactGroupFaToCollectionIfMissing(contactGroupCollection, contactGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactGroupFa to an array that doesn't contain it", () => {
          const contactGroup: IContactGroupFa = { id: 123 };
          const contactGroupCollection: IContactGroupFa[] = [{ id: 456 }];
          expectedResult = service.addContactGroupFaToCollectionIfMissing(contactGroupCollection, contactGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should add only unique ContactGroupFa to an array', () => {
          const contactGroupArray: IContactGroupFa[] = [{ id: 123 }, { id: 456 }, { id: 26026 }];
          const contactGroupCollection: IContactGroupFa[] = [{ id: 123 }];
          expectedResult = service.addContactGroupFaToCollectionIfMissing(contactGroupCollection, ...contactGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactGroup: IContactGroupFa = { id: 123 };
          const contactGroup2: IContactGroupFa = { id: 456 };
          expectedResult = service.addContactGroupFaToCollectionIfMissing([], contactGroup, contactGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactGroup);
          expect(expectedResult).toContain(contactGroup2);
        });

        it('should accept null and undefined values', () => {
          const contactGroup: IContactGroupFa = { id: 123 };
          expectedResult = service.addContactGroupFaToCollectionIfMissing([], null, contactGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should return initial array if no ContactGroupFa is added', () => {
          const contactGroupCollection: IContactGroupFa[] = [{ id: 123 }];
          expectedResult = service.addContactGroupFaToCollectionIfMissing(contactGroupCollection, undefined, null);
          expect(expectedResult).toEqual(contactGroupCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
