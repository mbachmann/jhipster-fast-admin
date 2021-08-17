import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactRelationDetailComponent } from './contact-relation-detail.component';

describe('Component Tests', () => {
  describe('ContactRelation Management Detail Component', () => {
    let comp: ContactRelationDetailComponent;
    let fixture: ComponentFixture<ContactRelationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactRelationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactRelation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactRelationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactRelationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactRelation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactRelation).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
