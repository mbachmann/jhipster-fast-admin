import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactFaDetailComponent } from './contact-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactFa Management Detail Component', () => {
    let comp: ContactFaDetailComponent;
    let fixture: ComponentFixture<ContactFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contact: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contact on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contact).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
