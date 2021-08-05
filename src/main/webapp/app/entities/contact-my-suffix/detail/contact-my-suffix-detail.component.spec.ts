import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactMySuffixDetailComponent } from './contact-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('ContactMySuffix Management Detail Component', () => {
    let comp: ContactMySuffixDetailComponent;
    let fixture: ComponentFixture<ContactMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contact: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactMySuffixDetailComponent);
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
