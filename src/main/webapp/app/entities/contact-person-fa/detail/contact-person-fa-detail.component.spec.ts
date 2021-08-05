import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactPersonFaDetailComponent } from './contact-person-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactPersonFa Management Detail Component', () => {
    let comp: ContactPersonFaDetailComponent;
    let fixture: ComponentFixture<ContactPersonFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactPersonFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactPerson: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactPersonFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactPersonFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactPerson on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactPerson).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
